package fr.utbm.lo53.wipos.server.offline;

import java.io.IOException;
 
public class Mesure extends HttpServlet {
 
    private int count;
 
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().log("init() called");
        count=0;
    }
 
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        getServletContext().log("service() called");
        count++;
        response.getWriter().write("Incrementing the count: Count = "+count);
    }
 
    @Override
    public void destroy() {
        getServletContext().log("destroy() called");
    }   
 
}