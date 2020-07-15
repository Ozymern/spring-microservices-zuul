package com.ozymern.spring.microservices.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostFilter extends ZuulFilter {


    private  static final Logger LOG= LoggerFactory.getLogger(PostFilter.class);


    //pre filtro, antes que se resuelva la ruta, se ocuparan palabras claves, estas son: pre, post, route ,error etc
    @Override
    public String filterType() {
        return "post";
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

        LOG.info(" post filter");
        //obteniendo el tiempo de inicio
        Long init = (Long) request.getAttribute("timeInit");

        Long end=System.currentTimeMillis();

        Long time=end - init;

        LOG.info(" post filter Titmpo transcurrido {}",time);
        return null;
    }
}
