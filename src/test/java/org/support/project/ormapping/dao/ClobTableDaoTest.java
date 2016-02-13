package org.support.project.ormapping.dao;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.support.project.common.log.Log;
import org.support.project.common.log.LogFactory;
import org.support.project.common.logic.H2DBServerLogic;
import org.support.project.common.util.StringUtils;
import org.support.project.ormapping.gen.dao.ClobTableDao;
import org.support.project.ormapping.gen.entity.ClobTableEntity;
import org.support.project.ormapping.tool.dao.InitializeDao;

public class ClobTableDaoTest {

	/** ログ */
	private static Log log = LogFactory.getLog(ClobTableDaoTest.class);

	@BeforeClass
	public static void checkDb() {
		H2DBServerLogic.get().start();
		InitializeDao dao = InitializeDao.get();
		//全テーブル削除
		dao.dropAllTable();
		// Webのデータベース登録
		dao.initializeDatabase("/ddl.sql");
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		H2DBServerLogic.get().stop();
	}

	@Test
	public void testDao() {
		ClobTableDao dao = ClobTableDao.get();
		
		ClobTableEntity entity = new ClobTableEntity();
		//entity.setContents("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		entity.setContents(StringUtils.randamGen(1024 * 1024));
		log.debug(entity);
		
		entity = dao.insert(entity);
		log.debug(entity);
		
		Assert.assertNotNull(entity.getNo());
		
		ClobTableEntity find = dao.selectOnKey(entity.getNo());
		org.junit.Assert.assertEquals(entity.getContents(), find.getContents());
		
		
	}

	@Test
	public void testSearch() {
		ClobTableDao dao = ClobTableDao.get();
		
		ClobTableEntity entity = new ClobTableEntity();
		entity.setContents("1234567890ABCDEFGH hoge  IJKLMNOPQRSTUVWXYZ");
		log.info(entity);
		entity = dao.insert(entity);
		log.info(entity);
		Assert.assertNotNull(entity.getNo());
		
		List<ClobTableEntity> finds = dao.searchContent("%hoge%");
		log.info(finds);
		org.junit.Assert.assertFalse(finds.isEmpty());
		
		//少なくともH2 Databaseではtextのカラムを検索出来るようだ
		
	}
	
}
