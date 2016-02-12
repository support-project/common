package org.support.project.common.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.logic.H2DBServerLogic;
import org.support.project.di.Container;

@RunWith(OrderedRunner.class)
public class TestCase {
	/** ログ */
	protected Log LOG = LogFactory.getLog(TestCase.class);
	
	public TestCase() {
		super();
		LOG = LogFactory.getLog(this.getClass());
	}
	
	@Rule
	public TestWatcher watchman = new TestWatcher();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		H2DBServerLogic logic = Container.getComp(H2DBServerLogic.class);
		logic.start();
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		H2DBServerLogic logic = Container.getComp(H2DBServerLogic.class);
		//logic.stop();
	}
	
	@Test
	public void test() {
		//何もしない
	}

}
