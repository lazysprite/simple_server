package test;

import server.core.codec.Packet;
import server.core.codec.Protocol;
import server.core.hotswap.HotSwapProxy;
import server.core.session.ChannelSession;

import java.util.List;

/**
 * Created by Administrator on 2016/5/8.
 */
public class ReqHelloWorld extends Packet {

    private String helloWorld = "hello world.";

    private int i;

    private int[] arr;

    public String[] sarr;

    private HelloStruct hs;

    private List<HelloStruct> list;

    private List<int[]> ii;

    private String ns;
    private HelloStruct nh;
    private String[] nsa;
    private List<int[]> nla;
    private int end;

    @Override
    public Protocol executePacket(ChannelSession session) {
        return HotSwapProxy.getInterface(ManagerInterface.class).handlerRequest(this);
    }


    public String getHelloWorld() {
        return helloWorld;
    }

    public void setHelloWorld(String helloWorld) {
        this.helloWorld = helloWorld;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public String[] getSarr() {
        return sarr;
    }

    public void setSarr(String[] sarr) {
        this.sarr = sarr;
    }

    public HelloStruct getHs() {
        return hs;
    }

    public void setHs(HelloStruct hs) {
        this.hs = hs;
    }

    public List<HelloStruct> getList() {
        return list;
    }

    public void setList(List<HelloStruct> list) {
        this.list = list;
    }

    public List<int[]> getIi() {
        return ii;
    }

    public void setIi(List<int[]> ii) {
        this.ii = ii;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public HelloStruct getNh() {
        return nh;
    }

    public void setNh(HelloStruct nh) {
        this.nh = nh;
    }

    public String[] getNsa() {
        return nsa;
    }

    public void setNsa(String[] nsa) {
        this.nsa = nsa;
    }

    public List<int[]> getNla() {
        return nla;
    }

    public void setNla(List<int[]> nla) {
        this.nla = nla;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
