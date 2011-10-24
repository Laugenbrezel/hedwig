package de.ikano.hedwig.util;

import java.io.IOException;

import javax.management.remote.rmi.RMIConnection;


public class JMXUtils {

	public static void closeQuietly (RMIConnection conn){
        if (conn != null) {
            try {
                conn.close();
            }
            catch (IOException ex) {
                //ok
            }
        }
	}
}
