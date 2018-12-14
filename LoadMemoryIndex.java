
import java.io.File;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.lucene.LuceneSail;
import org.openrdf.sail.lucene.LuceneSailSchema;
import org.openrdf.sail.memory.MemoryStore;
 
/**
 * LuceneSail   基于内存 memoryStore 生成索引
 */
public class LoadMemoryIndex {
	public static void main(String[] args) throws Exception {
		createSimple();	
	}
	
	/**
	 * 创建 LuceneSail，并执行查询 
	 */
	public static void createSimple() throws Exception {
		long s1 = System.currentTimeMillis();
		// 创建 sesame memory sail
		String file_path = "/home/LuceneSailMemo/chebi";   // 生成索引的路径
		File dataDir = new File(file_path);
		LuceneSail lucenesail = new LuceneSail();
		MemoryStore memoryStore = new MemoryStore(dataDir);
		Scanner sc = new Scanner(System.in);
		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");
		// set this parameter to store the lucene index on disk
		lucenesail.setParameter(LuceneSail.LUCENE_DIR_KEY, file_path);
		System.out.println("-------"+memoryStore.getDataDir());
		lucenesail.setBaseSail(memoryStore); 
		System.out.print("----------  memoryStore   -----------");	 
		SailRepository repository =  new SailRepository(lucenesail);
		repository.initialize();	
		System.out.print("----------  repository   -----------");
		SailRepositoryConnection connection = repository.getConnection();
		try {
		  System.out.print("----------  connection   -----------");
 	   	File r1 = new File("/home/data/ChEBI/chebi.n3"); // 数据的路径
  	 	connection.add(r1, "", RDFFormat.N3);  // 这里需要改成和数据格式对应
      // .n3 -> RDFFormat.N3      .nt -> RDFFormat.NTRIPLES    .rdf -> RDFFormat.RDFXML
		  System.out.println(" con n "+connection.size());
		
		  connection.commit();  // 提交
		  long s2 = System.currentTimeMillis();
		  System.out.println("evaluation time = " + (s2 - s1));	
		} finally {
			System.out.println("------- close ------");
			connection.close();
			repository.shutDown();
		}
		
	}
}
