package com.athaydes.gradle.osgi

import groovy.transform.ToString

/**
 * The configuration used by the {@code osgi-run} plugin to create and run OSGi environments.
 */
@ToString( includeFields = true, includeNames = true )
class OsgiConfig {
    // internal
    protected File outDirFile

    // non platform-dependent defaults
    def outDir = "osgi"
    String javaArgs = ""
    String programArgs = ""

    // platform dependent properties
    String configSettings
    String bundlesPath
    def bundles
    def osgiMain
    Map config

    OsgiConfig() {
        setConfigSettings 'felix'
    }

    void setConfigSettings( String option ) {
        this.configSettings = option
        switch ( option ) {
            case 'felix': configFelix()
                break
            case 'equinox': configEquinox()
                break
            case 'knopflerfish': configKnopflerfish()
                break
            default:
                configNone()
        }
    }

    void configFelix() {
        bundlesPath = 'bundle'
        bundles = FELIX_GOGO_BUNDLES
        osgiMain = FELIX
        config = [ 'felix.auto.deploy.action'  : 'install,start',
                   'felix.log.level'           : 1,
                   'org.osgi.service.http.port': 8080,
                   'obr.repository.url'        : 'http://felix.apache.org/obr/releases.xml' ]
    }

    void configEquinox() {
        bundlesPath = 'plugins'
        bundles = [ ]
        osgiMain = EQUINOX
        config = [ 'eclipse.ignoreApp': true,
                   'osgi.noShutdown'  : true ]
    }

    void configKnopflerfish() {
        bundlesPath = 'jars'
        bundles = [ ]
        osgiMain = KNOPFLERFISH

        config = [
                '-Dorg.knopflerfish.framework.main.verbosity'   : 0,
                '-Forg.knopflerfish.framework.debug.resolver'   : false,
                '-Forg.knopflerfish.framework.debug.errors'     : true,
                '-Forg.knopflerfish.framework.debug.classloader': false,
                '-Forg.osgi.framework.system.packages.extra'    : '',
                '-Forg.knopflerfish.startlevel.use'             : true,
                '-init'                                         : '',
                '-launch'                                       : '',
        ]
    }

    void configNone() {
        bundlesPath = 'bundle'
        bundles = [ ]
        osgiMain = FELIX
        config = [ : ]
    }

    // CONSTANTS

    static final String FELIX = 'org.apache.felix:org.apache.felix.main:5.4.0'

    static final String EQUINOX = 'org.eclipse.osgi:org.eclipse.osgi:3.7.1'

    static final String KNOPFLERFISH = 'org.knopflerfish:framework:7.1.2'

    static final FELIX_GOGO_BUNDLES = [
            'org.apache.felix:org.apache.felix.gogo.runtime:0.16.2',
            'org.apache.felix:org.apache.felix.gogo.shell:0.12.0',
            'org.apache.felix:org.apache.felix.gogo.command:0.16.0',
    ].asImmutable()

    static final IPOJO_BUNDLE = [
            'org.apache.felix:org.apache.felix.ipojo:1.12.1'
    ].asImmutable()

    static final IPOJO_ALL_BUNDLES = IPOJO_BUNDLE + [
            'org.apache.felix:org.apache.felix.shell:1.4.3',
            'org.apache.felix:org.apache.felix.shell.tui:1.4.1',
            'org.apache.felix:org.apache.felix.bundlerepository:2.0.6',
            'org.apache.felix:org.apache.felix.ipojo.arch:1.6.0'
    ].asImmutable()

}
