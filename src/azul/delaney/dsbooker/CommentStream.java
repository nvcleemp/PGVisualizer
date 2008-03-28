/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney.dsbooker;

import java.io.PrintStream;
import java.util.Locale;

/**
 *
 * @author nvcleemp
 */
public class CommentStream extends PrintStream {
    
    private PrintStream delegate;
    private String commentMarker;

    public CommentStream(PrintStream delegate, String commentMarker) {
        super(delegate);
        this.delegate = delegate;
        this.commentMarker = commentMarker;
    }

    @Override
    public void println(Object x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(String x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(char[] x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(double x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(float x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(long x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(int x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(char x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println(boolean x) {
        delegate.println(commentMarker + x);
    }

    @Override
    public void println() {
        delegate.println(commentMarker);
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        return delegate.printf(l, format, args);
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        return delegate.printf(format, args);
    }

    @Override
    public void print(Object obj) {
        delegate.print(obj);
    }

    @Override
    public void print(String s) {
        delegate.print(s);
    }

    @Override
    public void print(char[] s) {
        delegate.print(s);
    }

    @Override
    public void print(double d) {
        delegate.print(d);
    }

    @Override
    public void print(float f) {
        delegate.print(f);
    }

    @Override
    public void print(long l) {
        delegate.print(l);
    }

    @Override
    public void print(int i) {
        delegate.print(i);
    }

    @Override
    public void print(char c) {
        delegate.print(c);
    }

    @Override
    public void print(boolean b) {
        delegate.print(b);
    }
    
    

}
