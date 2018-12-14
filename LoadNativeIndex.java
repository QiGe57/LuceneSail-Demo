import java.io.File;
import java.util.Scanner;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.lucene.LuceneSail;
import org.openrdf.sail.lucene.LuceneSailSchema;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.nativerdf.NativeStore;
/**
 * 基于nativeStore生成索引
 */
public class LoadNativeIndex {
	public static void main(String[] args) throws Exception {		 
		 String index_path = "/home/LuceneSailMemo/dbpedia";	 // 生成索引的路径
		 createSimple(index_path);	
	}
 
	/**
	 * Create a LuceneSail and add some triples to it, ask a query.
	 */
	public static void createSimple(String index_path ) throws Exception {
		// create a sesame memory sail
		NativeStore myStore = new NativeStore();
		File dataDir = new File(index_path);
		myStore.setDataDir(dataDir);
		// create a lucenesail to wrap the memorystore
		LuceneSail lucenesail = new LuceneSail();
		// set this parameter to let the lucene index store its data in ram
		lucenesail.setParameter(LuceneSail.LUCENE_DIR_KEY, "true");
		// set this parameter to store the lucene index on disk
		// lucenesail.setParameter(LuceneSail.LUCENE_DIR_KEY, "./data/mydirectory");
 
		// wrap memorystore in a lucenesail
		lucenesail.setBaseSail(myStore);
		// create a Repository to access the sails
		SailRepository repository = new SailRepository(lucenesail);
		repository.initialize();
		SailRepositoryConnection connection = repository.getConnection();
		// connection.begin();
		try {
			 // connection.setAutoCommit(false);
			 // System.out.println(System.getProperty("user.dir"));   
			 String file_path = "/"; // 数据的路径
       File file = new File(file_path);
			 System.out.println(file.exists());
       connection.add(file, "", RDFFormat.NTRIPLES);	  // 这里需要改成和数据格式对应
       // .n3 -> RDFFormat.N3      .nt -> RDFFormat.NTRIPLES    .rdf -> RDFFormat.RDFXML
			connection.commit();
			System.out.println("------ 指数文件已生成 -----");
		} finally {
			connection.close();
			repository.shutDown();
		}
	}
}
