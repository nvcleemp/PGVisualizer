/* CommentStream.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.delaney.dsbooker;

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
