/*
 * This file is part of APLP: KIGB, licensed under the MIT License
 *
 * Copyright (c) 2022 TheSilkMiner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.thesilkminer.mc.austin

import groovy.transform.CompileStatic
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.forgespi.language.ILifecycleEvent
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.function.Consumer
import java.util.function.Supplier

@CompileStatic
final class AustinLanguageProvider implements IModLanguageProvider {
    private static final class AustinLanguageLoader implements IModLanguageLoader {
        private final String modId

        private AustinLanguageLoader(final String modId) {
            this.modId = modId
        }

        @Override
        <T> T loadMod(final IModInfo info, final ModFileScanData modFileScanResults, final ModuleLayer layer) {
            try {
                final containerName = 'net.thesilkminer.mc.austin.AustinLanguageProvider$AustinModContainer'
                final containerClass = Class.forName(containerName, true, Thread.currentThread().contextClassLoader)
                final constructor = containerClass.getConstructor(IModInfo, String)
                final instance = constructor.newInstance(info, this.modId)
                return instance as T
            } catch (final ReflectiveOperationException e) {
                LOGGER.fatal('Unable to load internal containers')
                throw e
            }
        }
    }

    @SuppressWarnings('unused')
    private static final class AustinModContainer extends ModContainer {
        private final String modId

        AustinModContainer(final IModInfo info, final String modId) {
            super(info)
            this.modId = modId
            this.contextExtension = { -> this.mod }
        }

        @Override
        boolean matches(final Object mod) {
            this.modId == mod
        }

        @Override
        Object getMod() {
            this.modId
        }
    }

    @SuppressWarnings('SpellCheckingInspection') private static final String NAME = '_austin'

    private static final Logger LOGGER = LogManager.getLogger(AustinLanguageProvider)
    private static final List<String> KNOWN_MODS = ['austin', 'paint']

    AustinLanguageProvider() {
        LOGGER.info('Successfully initialized Austin Language Provider for hard-coded containers')
    }

    @Override
    String name() {
        NAME
    }

    @Override
    Consumer<ModFileScanData> getFileVisitor() {
        return { ModFileScanData scanData ->
            final Map<String, AustinLanguageLoader> hardCodes = [:]
            scanData.IModInfoData.each { info ->
                info.mods.each { mod ->
                    final modId = mod.modId
                    if (modId in KNOWN_MODS) {
                        LOGGER.info('Identified known mod "{}"', modId)
                        hardCodes[modId] = new AustinLanguageLoader(modId)
                    }
                }
            }
            scanData.addLanguageLoader(hardCodes)
        }
    }

    @Override
    <R extends ILifecycleEvent<R>> void consumeLifecycleEvent(final Supplier<R> consumeEvent) {}
}
