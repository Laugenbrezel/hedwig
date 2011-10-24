/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ikano.hedwig.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.ikano.hedwig.controller.PatrolBean;
import de.ikano.hedwig.controller.TargetHome;
import de.ikano.hedwig.data.TargetListProducer;
import de.ikano.hedwig.model.PatrolReport;
import de.ikano.hedwig.model.Target;
import de.ikano.hedwig.model.TargetStatus;
import de.ikano.hedwig.util.JMXUtils;
import de.ikano.hedwig.util.Resources;

/**
 *
 * @author lutz
 */
@RunWith(Arquillian.class)
public class PatrolBeanTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war").
                addClasses(Target.class, PatrolReport.class,
                PatrolBean.class, TargetListProducer.class, TargetHome.class, TargetStatus.class, 
                Resources.class, JMXUtils.class).
                addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml").
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @Inject
    private PatrolBean patrolBean;
    @Inject
    Logger log;

    @Test
    public void testCheckSingleTarget() throws Exception {
        assertNotNull(patrolBean);
        
        Target target = new Target();
        target.setHost("localhost");
        target.setPort(8888);
        target.setRegistryName("jmxrmi");
        target.setObjectName("bean:name=sampleBean");
        target.setAccessMethod("checkMeOut");
        
        PatrolReport report = patrolBean.checkSingleTarget(target);
        assertNotNull(report);
        assertNotNull(report.getTarget());
        assertTrue(report.getStatus().equals(report.getTarget().getStatus()));
    }

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().
                getDeclaringClass());
    }
}
