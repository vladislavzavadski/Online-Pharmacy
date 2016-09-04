package by.training.online_pharmacy.controller.filter;

import by.training.online_pharmacy.command.Command;
import by.training.online_pharmacy.command.impl.Parameter;
import by.training.online_pharmacy.controller.CommandHelper;
import by.training.online_pharmacy.controller.CommandName;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.training.online_pharmacy.controller.CommandName.GET_PROFILE_IMAGE;

/**
 * Created by vladislav on 03.09.16.
 */
public class ControllerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean isConnectionEnable = CommandName.valueOf(servletRequest.getParameter(Parameter.COMMAND))!=GET_PROFILE_IMAGE;
        if(isConnectionEnable) {
            Command command = CommandHelper.getCommand(CommandName.RESERVE_CONNECTION);
            command.execute((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        if(isConnectionEnable) {
            Command command = CommandHelper.getCommand(CommandName.FREE_CONNECTION);
            command.execute((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
