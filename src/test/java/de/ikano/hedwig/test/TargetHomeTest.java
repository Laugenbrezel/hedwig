package de.ikano.hedwig.test;

import static org.junit.Assert.*;

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

import de.ikano.hedwig.controller.TargetHome;
import de.ikano.hedwig.model.Target;
import de.ikano.hedwig.model.TargetStatus;
import de.ikano.hedwig.util.Resources;

@RunWith(Arquillian.class)
public class TargetHomeTest {
   @Deployment
   public static Archive<?> createTestArchive() {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Target.class, TargetHome.class, TargetStatus.class, Resources.class)
            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Inject
   TargetHome targetHome;

   @Inject
   Logger log;

   @Test
   public void testRegister() throws Exception {
      Target newTarget = targetHome.getTarget();
      newTarget.setName("Jane Doe");
      newTarget.setHost("localhost");
      newTarget.setPort(3333);
      newTarget.setObjectName("samplename");
      newTarget.setAccessProperty("tudelu");
      targetHome.save();
      assertNotNull(newTarget.getId());
      assertNotNull(newTarget.getName());
      assertEquals(TargetStatus.UNKNOWN, newTarget.getStatus());
      log.info(newTarget.getName() + " was persisted with id " + newTarget.getId());
   }

   @Produces
   public Logger produceLog(InjectionPoint injectionPoint) {
      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
   }
   
}
