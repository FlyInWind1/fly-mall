package fly.spring.common.cdc.spring.controller;

import fly.spring.common.cdc.spring.ChangeListenerKeepAliveScheduledTask;
import fly.spring.common.pojo.R;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@ConditionalOnWebApplication
@RequestMapping("changeListener")
public class ChangeListenerController {
    private final ChangeListenerKeepAliveScheduledTask changeListenerKeepAliveScheduledTask;

    @GetMapping("keepAlive")
    public R<Object> keepAlive() {
        changeListenerKeepAliveScheduledTask.keepAlive();
        return R.ok();
    }
}
