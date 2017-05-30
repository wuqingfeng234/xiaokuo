/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.git.wuqf.xiaokuo.common.serialize;

import java.io.IOException;

/**
 * Data output.
 *
 * @author qian.lei
 */
public interface DataOutput {

    /**
     * Write boolean.
     *
     * @param v value.
     * @throws IOException
     */
    void writeBool(boolean v) throws IOException;

    /**
     * Write byte.
     *
     * @param v value.
     * @throws IOException
     */
    void writeByte(byte v) throws IOException;

    /**
     * Write short.
     *
     * @param v value.
     * @throws IOException
     */
    void writeShort(short v) throws IOException;

    /**
     * Write integer.
     *
     * @param v value.
     * @throws IOException
     */
    void writeInt(int v) throws IOException;

    /**
     * Write long.
     *
     * @param v value.
     * @throws IOException
     */
    void writeLong(long v) throws IOException;

    /**
     * Write float.
     *
     * @param v value.
     * @throws IOException
     */
    void writeFloat(float v) throws IOException;

    /**
     * Write double.
     *
     * @param v value.
     * @throws IOException
     */
    void writeDouble(double v) throws IOException;

    /**
     * Write string.
     *
     * @param v value.
     * @throws IOException
     */
    void writeUTF(String v) throws IOException;

    /**
     * Write byte array.
     *
     * @param v value.
     * @throws IOException
     */
    void writeBytes(byte[] v) throws IOException;

    /**
     * Write byte array.
     *
     * @param v   value.
     * @param off offset.
     * @param len length.
     * @throws IOException
     */
    void writeBytes(byte[] v, int off, int len) throws IOException;

    /**
     * Flush buffer.
     *
     * @throws IOException
     */
    void flushBuffer() throws IOException;
}