package test;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import com.ritho.hbase.util.HBaseUtilities;
/***
 * Just an outline of basic HBaseUtilities.java usages.
 * 
 * @author kaniu ndungu
 * **/
public class TestHBaseUtil {

	HBaseConfiguration config = null;

	@Before
	public void configureRemoteHBase() throws IOException {

		config = (HBaseConfiguration) HBaseConfiguration.create();
		config.clear();
		config.set("hbase.zookeeper.quorum", "127.0.0.1");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		config.set("hbase.master", "127.0.0.1:60000");

		HBaseAdmin.checkHBaseAvailable(config);

	}

	@Test
	public void testReadRowVersions() throws IOException,
			ClassNotFoundException {

		String tableName = "neurojobTable";

		HTable table = new HTable(config, tableName);

		List<KeyValue> n_indexVersions = HBaseUtilities.getSingleton()
				.getVersionedRowColumnData(table, "row_name", "family",
						"qualifier");
		System.out.println("n_indexVersions size ='" + n_indexVersions.size()
				+ "'");

		table.close();

	}

	@Test
	public void testWriteData() throws IOException, ClassNotFoundException {

		String tableName = "newTableName";

		HBaseUtilities.getSingleton().addColumn(config, tableName, "family");

		// create table instance
		HTable table = new HTable(config, tableName);

		HBaseUtilities.getSingleton().addData(table, "row1", "family",
				"myqualifiercolumn", Bytes.toBytes("test value 1234"));

	}

	// @Test
	public void testDeleteData() throws IOException, ClassNotFoundException {

		String tableName = "newTableName";

		// create table instance
		HTable table = new HTable(config, tableName);

		HBaseUtilities.getSingleton().deleteColumn(table, "row1", "family",
				"myqualifiercolumn");

		HBaseUtilities.getSingleton().deleteRow(table, "row1");

	}

	@Test
	public void testScanData() throws IOException, ClassNotFoundException {

		String tableName = "newTableName";

		HTable table = new HTable(config, tableName);

		Scan scan = new Scan();

		SingleColumnValueFilter filter1 = new SingleColumnValueFilter(Bytes
				.toBytes("sampledata"), Bytes.toBytes("rid"),
				CompareOp.GREATER, Bytes.toBytes(0));
		filter1.setFilterIfMissing(true);
		SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes
				.toBytes("sampledata"), Bytes.toBytes("rid"),
				CompareOp.LESS_OR_EQUAL, Bytes.toBytes(100));
		filter2.setFilterIfMissing(true);

		scan.setFilter(new FilterList(FilterList.Operator.MUST_PASS_ALL,
				filter1, filter2));
		scan.setMaxVersions(1);
		scan.setCaching(50); // 1 is the default in Scan, which will be bad for
								// MapReduce jobs
		scan.setCacheBlocks(false); // don't set to true for MR jobs

		List<Result> resultsList = HBaseUtilities.getSingleton()
				.scanRowColumnData(table, scan);

		int i = 0;

		for (Result result : resultsList) {

			System.out.println(i++
					+ "  :  row="
					+ Bytes.toString(result.getRow())
					+ "  :  id = "
					+ Bytes.toInt(result.getValue(Bytes.toBytes("sampledata"),
							Bytes.toBytes("rid"))));

		}
	}

}
