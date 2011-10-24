/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ikano.hedwig.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author lutz
 */
public class TestMain {

    public static void main(String args[]) {
        try {
            JMXServiceURL url =
                    new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8888/jmxrmi");
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        }
        catch (IOException ex) {
            Logger.getLogger(TestMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
