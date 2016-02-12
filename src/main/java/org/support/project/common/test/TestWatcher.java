package org.support.project.common.test;

import java.util.Date;

import org.junit.runner.Description;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;

public class TestWatcher extends org.junit.rules.TestWatcher {
	/** ログ */
	private static Log LOG = LogFactory.getLog(TestWatcher.class);

	private Date start;
	@Override
	protected void starting(Description description) {
		super.starting(description);
		start = new Date();
		LOG.info("@@@@@@ TEST START - " + description.getClassName() + "#" + description.getMethodName());
	}
	
	@Override
	protected void finished(Description description) {
		super.finished(description);
		Date end = new Date();
		long time = end.getTime() - start.getTime();
		LOG.info("@@@@@@ TEST FINISHED - "  + time + " [ms]" + "  -  " + description.getClassName() + "#" + description.getMethodName());
	}
}