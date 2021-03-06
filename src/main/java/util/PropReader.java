/*
 * Copyright 2016-2017 Ague Mort of Veteran Software
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package util;

import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author keesh
 */
public final class PropReader {

    private static final String fileName = "./config.properties";
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(PropReader.class);
    private static PropReader instance = new PropReader();
    private Properties prop;

    private PropReader() {

        InputStream input = null;

        prop = new Properties();

        try {
            input = new FileInputStream(fileName);

            try {
                prop.load(input);
            } catch (IOException e) {
                logger.error("Failed to read 'config.properties'.", e);
            }
        } catch (FileNotFoundException e) {
            logger.error("File 'config.properties' not found.");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("Failed to close the input stream from 'config.properties'", e);
                }
            }
        }
    }

    public static PropReader getInstance() {
        return instance;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties property) {
        this.prop = property;
    }
}
