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

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@CompileStatic
@PackageScope
final class BootDataManager {
    private static final String IMPL_VERSION = 'Implementation-Version: '

    @PackageScope
    static final BootDataManager INSTANCE = new BootDataManager()

    @Lazy
    Path own = { Paths.get this.class.protectionDomain.codeSource.location.toURI() }()

    @Lazy
    String version = {
        final manifestPath = this.own.resolve('META-INF/MANIFEST.MF')
        final lines = Files.readAllLines(manifestPath)
        lines.find { it.startsWith(IMPL_VERSION) }
                ?.substring(IMPL_VERSION.length())
                ?.trim()
                ?: '1.0.0'
    }()

    final String license = 'MIT'

    @Lazy
    Set<String> packages = {
        try (final walker = Files.walk(this.own)) {
            walker.toList()
                    .findAll { it.nameCount > 0 }
                    .findAll { it.getName(0).toString() != 'META-INF' }
                    .findAll { it.fileName.toString().endsWith('.class') }
                    .findAll(Files.&isRegularFile)
                    .collect { it.parent.toString().replace([ '/' : '.' ] as Map<CharSequence, CharSequence>) }
                    .findAll { !it.isEmpty() }
                    .toSet()
        }

    }()

    private BootDataManager() {}
}
