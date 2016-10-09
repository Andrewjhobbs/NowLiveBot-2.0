/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
public class PropReader {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(PropReader.class);
    private static PropReader instance = new PropReader();

    private Properties prop;

    private PropReader() {

        InputStream input = null;

        prop = new Properties();

        try {
            input = new FileInputStream("./config.properties=");
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

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}