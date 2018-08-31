package fi.muke.jeitodo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class JeiErrorController implements ErrorController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String PATH = "/error";

    private final ErrorAttributes errorAttributes;

    public JeiErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(value = PATH)
    public String error(WebRequest request, Model model) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, true);

        log.info("Error: {}", errorAttributes.get("message"));

        model.addAttribute("errorAttributes", errorAttributes);
        model.addAttribute("errorMessage", errorAttributes.get("message"));

        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        return errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}
