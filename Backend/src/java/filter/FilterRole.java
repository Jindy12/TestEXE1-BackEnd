package filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;

public class FilterRole implements Filter {

    private static final boolean debug = true;
    private FilterConfig filterConfig = null;

    public FilterRole() {
    }

    // Method to return URLs for different role-based requests
    private List<String> getAdminUrls() {
        return Arrays.asList("/userdetails", "/admin", "/settingsList","/userlist","addUser","/settingdetail");
    }

    private List<String> getAdminMarketingUrls() {
        return Arrays.asList("/sliderDetails", "/addcustomer", "/customerlist", "/postlist", 
                             "/postdetail", "/productDetails", "/feedbacksList", "/dash", "/proformarketing","/SliderList","/editproductbymarketing");
    }

    private List<String> getSaleAdminUrls() {
        return Arrays.asList("/orderDetailsSale", "/orderlist", "/sale","/salecheck");
    }

    private List<String> getMarketingUrls() {
        return Arrays.asList("/marketing","/marketingdashboard");
    }
    private List<String> getShipperUrls() {
        return Arrays.asList("/shipper");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/error.jsp";
        String requestURI = req.getRequestURI();
        
        boolean loggedIn = session != null && session.getAttribute("account") != null;
        boolean loginRequest = requestURI.equals(loginURI);

        if (loggedIn) {
            Account account = (Account) session.getAttribute("account");
            int roleId = account.getRoleId();

            // Check for admin requests
            if (getAdminUrls().stream().anyMatch(requestURI::contains) && roleId != 1) {
                res.sendRedirect(loginURI);
            }
            // Check for admin + marketing requests
            else if (getAdminMarketingUrls().stream().anyMatch(requestURI::contains) && roleId != 1 && roleId != 2) {
                res.sendRedirect(loginURI);
            }
            // Check for sales + admin requests
            else if (getSaleAdminUrls().stream().anyMatch(requestURI::contains) && roleId != 1 && roleId != 3) {
                res.sendRedirect(loginURI);
            }
            // Check for marketing requests
            else if (getMarketingUrls().stream().anyMatch(requestURI::contains) && roleId != 2) {
                res.sendRedirect(loginURI);
            }else if (getShipperUrls().stream().anyMatch(requestURI::contains) && roleId != 5) {
                res.sendRedirect(loginURI);
            } else {
                chain.doFilter(request, response);
            }
        } else if (loginRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}
