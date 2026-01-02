package dev.gruff.jpms.tests;

import dev.gruff.jpms.ExportedClass;
import dev.gruff.jpms.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class TestPackageNotExported {

    @Test
    public void testPackageNotExported() {
        // We attempt to access a class in the library.
        // In V1, the package is exported, so access works.
        // In V2, the package is NOT exported, so access throws IllegalAccessError.
        
        boolean accessSucceeded = false;
        try {
            ExportedClass.call();
            accessSucceeded = true;
            
            // If access succeeded, we must verify we are NOT on the classpath.
            // If we are on the classpath, access succeeds even if we didn't want it to (in V2 context, though V2 shouldn't be on classpath).
            Module libModule = ExportedClass.class.getModule();
            if (libModule.getName() == null) {
                 fail("Library class loaded from Unnamed Module (Classpath). JPMS tests require Module Path execution.");
            }
            
        } catch (IllegalAccessError e) {
            // Expected in V2
            accessSucceeded = false;
        } catch (Throwable t) {
            // Catch any other error (LinkageError, etc)
            // System.out.println("Caught unexpected throwable: " + t.getClass().getName() + ": " + t.getMessage());
            if (t.getClass().getName().contains("IllegalAccess")) {
                 accessSucceeded = false;
            } else {
                throw t;
            }
        }

        // Check module configuration
        ModuleLayer layer = ModuleLayer.boot();
        java.util.Optional<Module> moduleOpt = layer.findModule("dev.gruff.jpms");
        
        if (moduleOpt.isPresent()) {
            Module module = moduleOpt.get();
            boolean isExported = module.isExported("dev.gruff.jpms");
            
            if (isExported) {
                // We are in V1
                if (!accessSucceeded) {
                    fail("Package is exported but access failed!");
                }
            } else {
                // We are in V2
                if (accessSucceeded) {
                    fail("Package is NOT exported but access succeeded!");
                }
            }
        } else {
             fail("Module 'dev.gruff.jpms' not found!");
        }
    }
}
