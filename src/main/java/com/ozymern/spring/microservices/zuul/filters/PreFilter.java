package com.ozymern.spring.microservices.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreFilter extends ZuulFilter {


    private  static final Logger LOG= LoggerFactory.getLogger(PreFilter.class);


    //pre filtro, antes que se resuleva la ruta, cuando sea pre es necesario especificar con el string pre
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    // validar si se ejecutara o no el filtro, si retorna true ejecuta el metodo run
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //resuelve la logica del filtro
    @Override
    public Object run() throws ZuulException {
        //obtengo el request
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        LOG.info(" pre filter {} request enrutado a {}",request.getMethod(),request.getRequestURI());
        //tiempo de inicio
        Long init = System.currentTimeMillis();
//agrego tiempo de inicio al request
        request.setAttribute("timeInit", init);

        return null;
    }
}
