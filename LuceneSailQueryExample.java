import java.io.File;
import java.net.URL;
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
 *   LuceneSail 关键字查找示例
 */
public class LuceneSailQueryExample {

	/**
	 * Create a lucene sail and use it
	 */
	public static void main(String[] args) throws Exception {

		String keyword = "18357";  //  要查找的关键字
		createSimple(keyword);
	}

	/**
	 * Create a LuceneSail and add some triples to it, ask a query.
	 */
	public static void createSimple(String keyword) throws Exception {
		// create a sesame memory sail		
 		File dataDir = new File("C:\\chebi");  // 导入索引的路径
		NativeStore myStore = new NativeStore(dataDir);
 
		LuceneSail lucenesail = new LuceneSail();
		lucenesail.setParameter(LuceneSail.LUCENE_DIR_KEY, "true");
	 
		lucenesail.setBaseSail(myStore);

		// create a Repository to access the sails
		SailRepository repository = new SailRepository(lucenesail);
		repository.initialize();

		// add some test data, the FOAF ont
		SailRepositoryConnection connection = repository.getConnection();
		// connection.begin();
		try {

			String queryString = "PREFIX search: <" + LuceneSailSchema.NAMESPACE + "> \n"
					+ "SELECT ?x ?score ?snippet WHERE { \n" + "?x search:matches ?m. \n" + "?m search:query \""
					+ keyword + "\";\n" + "search:score ?score; \n" + "search:snippet ?snippet. }";

			System.out.println("Running query: \n" + queryString);
			TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			TupleQueryResult result = query.evaluate();
			System.out.println("结果  :  " + result.hasNext());
			
			try {
				// print the results
				int count = 0;
				while (result.hasNext()) {
					BindingSet bindings = result.next();
					count ++;
					System.out.println(" ------------- ");
					for (Binding binding : bindings) {
						System.out.println("  " + binding.getName() + ": " + binding.getValue());
					}
				}
				System.out.println("found match: "+count);
			} finally {
				result.close();
			}
		} finally {
			connection.close();
			repository.shutDown();
		}

	}
}
