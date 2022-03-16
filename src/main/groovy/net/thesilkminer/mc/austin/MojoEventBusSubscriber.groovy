package net.thesilkminer.mc.austin

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.MapConstructor
import groovy.transform.PackageScope
import groovy.transform.VisibilityOptions
import groovy.transform.options.Visibility
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.Logging
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation
import net.minecraftforge.forgespi.language.ModFileScanData
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.objectweb.asm.Type

import java.lang.reflect.Method

@CompileStatic
@MapConstructor(includeFields = true)
@PackageScope
@VisibilityOptions(constructor = Visibility.PACKAGE_PRIVATE)
class MojoEventBusSubscriber {

    private static final Logger LOGGER = LogManager.getLogger(MojoEventBusSubscriber)
    private static final Type EVENT_BUS_SUBSCRIBER = Type.getType(EventBusSubscriber)

    @SuppressWarnings('SpellCheckingInspection')
    private static final String SUBSCRIBER_METHOD_NAME_BEGINNING = '$$aplp$synthetic$registerSubscribers'

    private MojoContainer mojoContainer
    private ModFileScanData scanData
    private ClassLoader loader

    @PackageScope
    void doSubscribing() {
        this.scanData.annotations
                .findAll { it.annotationType() == EVENT_BUS_SUBSCRIBER }
                .each(this.&subscribe)
    }

    private void subscribe(final AnnotationData data) {
        final String mojoId = data.annotationData().mojoId as String
        if (mojoId != this.mojoContainer.modId) return

        final EventBus bus = bus(data)
        final Set<Dist> distributions = distributions(data)

        if (FMLEnvironment.dist in distributions) {
            this.doSubscribe(bus, distributions, data.clazz())
        }
    }

    private void doSubscribe(final EventBus bus, final Set<Dist> distributions, final Type clazz) {
        try {
            LOGGER.debug(Logging.LOADING, 'Performing subscription of {} for {} onto bus {} with dist {}', clazz, this.mojoContainer.modId, bus, distributions)
            final Class<?> initializedClass = Class.forName(clazz.className, true, this.loader)
            final String name = "${SUBSCRIBER_METHOD_NAME_BEGINNING}__${bus.toString()}\$\$"
            final Method initMethod = initializedClass.getDeclaredMethod(name, Object)
            initMethod.invoke(null, this.mojoContainer.mod)
        } catch (final Throwable t) {
            LOGGER.fatal(Logging.LOADING, "Unable to load subscriber $clazz for ${this.mojoContainer.modId}", t)
            throw new RuntimeException(t)
        }
    }

    private static EventBus bus(final AnnotationData data) {
        final ModAnnotation.EnumHolder holder = data.annotationData().bus as ModAnnotation.EnumHolder
        EventBus.valueOf(holder.value)
    }

    @CompileDynamic
    private static Set<Dist> distributions(final AnnotationData data) {
        final List<ModAnnotation.EnumHolder> declaredHolders = data.annotationData().dist as List<ModAnnotation.EnumHolder>
        final List<ModAnnotation.EnumHolder> holders = declaredHolders ?: (this.&makeDefaultDistributionHolders)()
        holders.collect { Dist.valueOf(it.value) }.toSet()
    }

    private static List<ModAnnotation.EnumHolder> makeDefaultDistributionHolders() {
        Dist.values().collect { new ModAnnotation.EnumHolder(null, it.name()) }
    }
}
