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

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import net.minecraftforge.forgespi.language.IConfigurable
import net.minecraftforge.forgespi.language.IModFileInfo
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.locating.IModFile

@CompileStatic
@PackageScope
class LoaderMojoFileInfo implements IModFileInfo {
    final IModFile file

    @PackageScope
    LoaderMojoFileInfo(final IModFile file) {
        this.file = file
    }

    @Override
    List<IModInfo> getMods() {
        []
    }

    @Override
    List<LanguageSpec> requiredLanguageLoaders() {
        []
    }

    @Override
    boolean showAsResourcePack() {
        false
    }

    @Override
    Map<String, Object> getFileProperties() {
        [:]
    }

    @Override
    String getLicense() {
        BootDataManager.INSTANCE.license
    }

    @Override
    String moduleName() {
        this.file.secureJar.name()
    }

    @Override
    String versionString() {
        BootDataManager.INSTANCE.version
    }

    @Override
    List<String> usesServices() {
        []
    }

    @Override
    IModFile getFile() {
        this.file
    }

    @Override
    IConfigurable getConfig() {
        new IConfigurable() {
            @Override
            <T> Optional<T> getConfigElement(final String... key) {
                Optional.empty()
            }

            @Override
            List<? extends IConfigurable> getConfigList(final String... key) {
                []
            }
        }
    }
}
