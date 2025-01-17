package ua.pp.lumivoid.redstonehelper.util

import ua.pp.lumivoid.redstonehelper.Constants
import java.util.concurrent.ConcurrentLinkedQueue

object TickHandler {
    private val logger = Constants.LOGGER

    private val taskQueue: ConcurrentLinkedQueue<DelayedTask> = ConcurrentLinkedQueue()

    fun scheduleAction(delayTicks: Int, action: () -> Unit) {
        logger.debug("Scheduling action with delay: $delayTicks ticks")
        taskQueue.add(DelayedTask(delayTicks, action))
    }

    fun onEndTick() {
        val iterator = taskQueue.iterator()
        while (iterator.hasNext()) {
            val task = iterator.next()
            task.ticksLeft--
            if (task.ticksLeft <= 0) {
                task.action()
                iterator.remove()
            }
        }
    }
}

data class DelayedTask(var ticksLeft: Int, val action: () -> Unit)