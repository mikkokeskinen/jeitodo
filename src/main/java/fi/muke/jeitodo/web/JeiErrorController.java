package fi.muke.jeitodo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class JeiErrorController implements ErrorController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView errorPage = new ModelAndView("error");

        Map<String, Object> errorAttributes = getErrorAttributes(request, true);

        errorPage.addObject("errorAttributes", errorAttributes);
        errorPage.addObject("errorMessage", errorAttributes.get("message"));

        log.info("Error: {}", errorAttributes.get("message"));

        return errorPage;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
}
