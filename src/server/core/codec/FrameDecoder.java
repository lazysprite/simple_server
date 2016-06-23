package server.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import server.ServerConfig;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

/**
 * 可以做一些解码工作
 * 丢弃一个错误的协议，并且修正buffer中的内容，以至于
 * LengthFieldBaseFrameDecoder可以正确的进行解码工作
 * 与此同时进行相应的日志记录，方便对问题进行定位
 * 
 * 协议格式
 * ----------------------------------------------
 * |head	|length		|context	|tail		|
 * ----------------------------------------------
 * head: 协议头部(-81)
 * length: 协议长度
 * context: 协议内容
 * tail: 协议尾部(-82)
 * @author zhouyaohui
 *
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {

	private int maxFrameLength;
	/**
	 * 保存跳过的字节数
	 */
	private int skipByte = 0;
	private int length = 0;

	private InetSocketAddress remote;
	
	public FrameDecoder(ByteOrder byteOrder,
						int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
						int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
		super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength,
				lengthAdjustment, initialBytesToStrip, failFast);
		this.maxFrameLength = maxFrameLength;
	}

	public FrameDecoder(int maxFrameLength,
						int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
						int initialBytesToStrip, boolean failFast) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip, failFast);
		this.maxFrameLength = maxFrameLength;
	}

	public FrameDecoder(int maxFrameLength,
						int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
						int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip);
		this.maxFrameLength = maxFrameLength;
	}

	public FrameDecoder(int maxFrameLength,
						int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.maxFrameLength = maxFrameLength;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		return decode(ctx, in, false);
	}

	private Object decode(ChannelHandlerContext ctx, ByteBuf in, boolean retry)
			throws Exception {
		if (in.readableBytes() < 6) {
			return null;
		}
		
		skip(in);	// 跳到协议头部
		if (in.readableBytes() < 6) return null;
		
		/**
		 * 验证长度的合法性
		 */
		int length = in.getInt(in.readerIndex() + 1);
		if (length < 0) {
			in.skipBytes(1);	// 只需要跳过一个字节，然后返回交给第一步去处理
			skip(in);
			if (in.readableBytes() < 6) return null;
			if (!retry) return decode(ctx, in, true);	// 在尝试一下
		}
		if (length > this.maxFrameLength) {
			/**
			 * 需要将最大长度拦截下来，不然就容易导致混乱的解码
			 * 因为父类会对超过最大长度的协议进行丢弃，然后这个丢弃的过程可能存在多次的调用情况
			 * 所以当这种情况产生的时候会导致该方法进行修正，然而父类还是会进行丢弃相应的字节
			 * 导致解码失败
			 */
			in.skipBytes(1);	// 为什么不是直接跳过指定长度呢? 是因为长度的不对可能是协议错误
			skip(in);
			if (in.readableBytes() < 6) return null;
			if (!retry) return decode(ctx, in, true);	// 再次解码
		}
		if (length > in.readableBytes()) {
			return null;
		}
		
		byte tail = in.getByte(in.readerIndex() + length - 1);
		if (ServerConfig.CodecConfig.TAIL != tail) {
			/**
			 * 正在处理的协议是有问题的，长度域的值与实际的长度是不相符合的
			 * 需要做的是：去找到协议真正的长度(就是找到-82-81连着的两个字节的地方)
			 * 选择先向前搜索，因为向后搜索是无底洞
			 */
			int currentIndex = in.readerIndex() + length - 2;
			int endIndex = in.readerIndex() + 4;
			byte tmpHead = in.getByte(currentIndex + 1);
			byte tmpTail = 0;
			for ( ; currentIndex > endIndex; currentIndex--) {
				tmpTail = in.getByte(currentIndex);
				if (ServerConfig.CodecConfig.HEAD == tmpHead &&
						ServerConfig.CodecConfig.TAIL == tmpTail) {
					break;
				} else {
					tmpHead = in.getByte(currentIndex);
				}
			}
			if (currentIndex > endIndex) {
				// 找到真正的结束为止了,记录日志
				int trueLength = currentIndex - in.readerIndex() + 1;
				System.out.println("Field length: " + length + " acturely length: " + trueLength);
				in.skipBytes(trueLength);
				if (in.readableBytes() < 6) return null;
				if (!retry) return decode(ctx, in, true);	// 再次解码
			} else {
				in.skipBytes(length);
				this.skipByte += length;
				this.length  = length;
				skip(in);
				if (in.readableBytes() < 6) return null;
				if (!retry) return decode(ctx, in, true);	// 再次解码
			}
		}
		
		if (this.skipByte != 0) {
			System.out.println("Field length: " + this.length + " truely length: " + this.skipByte);
			this.skipByte = 0;
			this.length = 0;
		}
		
		return super.decode(ctx, in);
	}

	/**
	 * 跳到协议头
	 * @param in
	 */
	private void skip(ByteBuf in) {
		int readable = in.readableBytes();
		int readerIndex = in.readerIndex();
		byte head;
		byte tail = ServerConfig.CodecConfig.TAIL;
		int skip = 0;
		for (int i = 0; i < readable; i++) {
			byte tmp = head = in.getByte(readerIndex + i);
			if (ServerConfig.CodecConfig.HEAD == head && ServerConfig.CodecConfig.TAIL == tail) {
				break;
			} else {
				skip++;
				tail = tmp;
			}
		}
		if (skip != 0) {
			in.skipBytes(skip);
			if (this.skipByte != 0) this.skipByte += skip;
		}
	}
}
