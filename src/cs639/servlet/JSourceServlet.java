package cs639.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import cs639.jsource.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JSourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		// put any init code you need here (to be called before any requests come in) 
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		String contextPath = context.getContextPath();
		System.out.println(contextPath);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String requestURL = request.getServletPath(); // like "/hi.html"
		String fileName = requestURL.substring(1, requestURL.substring(1).lastIndexOf(".")+1); // past "/"
		if (fileName == null) {
			System.exit(0);
		}
		String extn = requestURL.substring(requestURL.substring(1).lastIndexOf(".")+1);
		if(extn.equals(".xml")){
			JavaSource source = new JavaSource(fileName);
			try {
				out.println(source.toXML(false, false)
					.toString());  // convert StringBuilder to String
			} catch (ClassNotFoundException e) {
				System.err.println(e);
			}
		}
		else if(extn.equals(".xsd")){
			InputStream in0;
			BufferedReader in = null;
			try {
				String fp = "WEB-INF/schema/"+fileName+extn;
				in0 = context.getResourceAsStream(fp);
				in = new BufferedReader(new InputStreamReader(in0, "UTF-8"));
				String line;
				while ((line = in.readLine()) != null)
					out.println(line);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		out.flush();
		out.close();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
