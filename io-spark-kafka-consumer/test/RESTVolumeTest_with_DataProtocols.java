
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RESTVolumeTest_with_DataProtocols {
	ExecutorService executor = null;
	{
		executor = Executors.newFixedThreadPool(100);
	}

	public static void main(String args[]) {

		RESTVolumeTest_with_DataProtocols producer = new RESTVolumeTest_with_DataProtocols();

		try {

			for (int i = 0; i < 10; i++) {
				// send lots of messages
				producer.send(String.format("{\"type\":\"fast\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i));

				// every so often send to a different topic
				if (i % 1000 == 0) {
					producer.send(
							String.format("{\"type\":\"request\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i));
					producer.send(
							String.format("{\"type\":\"log\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i));
					producer.send(
							String.format("{\"type\":\"notice\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i));
					producer.send(String.format("{\"type\":\"response\", \"t\":%.3f, \"k\":%d}",
							System.nanoTime() * 1e-9, i));

					System.out.println("Sent msg number " + i);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	public static URLConnection openConnection() throws IOException {
		URL url = new URL("http://ec2-00-00-00-00.us-west-2.compute.amazonaws.com:8090/gateway/queues");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization",
				"Bearer blahblahblah");
		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		return connection;
	}

	public void send(String message) {
		executor.submit(new Sender(message));
	}

	class Sender implements Callable<String> {
		String message = null;

		public Sender(String _message) {
			message = _message;
		}

		@Override
		public String call() throws Exception {
			StringBuilder resposneBuilder = new StringBuilder();
			try {
				URLConnection connection = RESTVolumeTest_with_DataProtocols.openConnection();
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
				out.write(message);
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String response = null;

				while ((response = in.readLine()) != null) {
					resposneBuilder.append(response).append(" ");
				}
				System.out.println("\nCrunchify REST Service Invoked Successfully..." + resposneBuilder);
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling Crunchify REST Service");
				e.printStackTrace();
			}
			return resposneBuilder.toString();
		}

	}
}
