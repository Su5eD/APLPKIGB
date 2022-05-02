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

package net.thesilkminer.mc.austin.boot

import cpw.mods.jarhandling.SecureJar
import net.minecraftforge.forgespi.locating.IModFile
import net.minecraftforge.forgespi.locating.IModLocator
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.nio.file.Path
import java.util.function.Consumer

final class AustinModLocator implements IModLocator {
    private static final Logger LOGGER = LogManager.getLogger(AustinModLocator)
    private static final String NAME = 'aplp:mod_locator'

    AustinModLocator() {
        LOGGER.info('Successfully initialized mod locator {}, ready to inject fake mods', NAME)
    }

    @Override
    List<IModFile> scanMods() {
        final modFiles = [
                MojoFile.makeLoaderModFile(this),
                MojoFile.makeGameModFile(this),
                MojoFile.makePaintModFile(this)
        ]
        LOGGER.info('Mod scanning has "identified" {} additional JARs to load: {}', modFiles.size(), modFiles)
        modFiles
    }

    @Override
    String name() {
        NAME
    }

    @Override
    void scanFile(final IModFile modFile, final Consumer<Path> pathConsumer) {
        modFile.setSecurityStatus(SecureJar.Status.VERIFIED)
    }

    @Override
    void initArguments(final Map<String, ?> arguments) {

    }

    @Override
    boolean isValid(final IModFile modFile) {
        true
    }
}
