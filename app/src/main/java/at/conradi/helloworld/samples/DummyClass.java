package at.conradi.helloworld.samples;

public class DummyClass {

    public Void someMethod() {
        throw new UnsupportedOperationException();
    }
    public int getAsInt() {
        return 2;
    }
    public String getAsString(){
        return String.valueOf(getAsInt());
    }
}
