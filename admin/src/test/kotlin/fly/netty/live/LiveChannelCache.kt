package fly.netty.live

import io.netty.channel.Channel
import io.netty.util.concurrent.ScheduledFuture

class LiveChannelCache(
    var channel: Channel,

    var scheduledFuture: ScheduledFuture<*>
)
