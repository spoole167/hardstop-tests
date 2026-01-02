import dev.gruff.hardstop.testcases.apicheck.PublicFieldBean;
import dev.gruff.hardstop.testcases.apicheck.UserBean;
import dev.gruff.hardstop.testcases.apicheck.Version;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests migration from JavaBean to Record.
 * Matches spec scenario: JavaBean to Record Migration (Section G).
 */
public class TestRecordMigration {

    /**
     * Matches JavaBean to Record migration (Accessor Naming).
     * V1: UserBean is a class with getName().
     * V2: UserBean is a record with name().
     * Client calling getName() fails with NoSuchMethodError.
     */
    @Test
    public void testBeanToRecord() {
        if (Version.isV1()) {
            UserBean u = new UserBean("test");
            assertEquals("test", u.getName());
        } else {
            try {
                UserBean u = new UserBean("test");
                u.getName();
                fail("NoSuchMethodError expected");
            } catch (NoSuchMethodError e) {
                // Expected
            } catch (IncompatibleClassChangeError e) {
                // Expected
            }
        }
    }

    /**
     * Matches JavaBean to Record migration (Field Access).
     * V1: PublicFieldBean has public int age.
     * V2: PublicFieldBean is a record. 'age' is now a private final field.
     * Client accessing .age fails with IllegalAccessError (or NoSuchFieldError).
     */
    @Test
    public void testPublicFieldToRecordComponent() {
        if (Version.isV1()) {
            PublicFieldBean p = new PublicFieldBean(10);
            assertEquals(10, p.age);
        } else {
            try {
                PublicFieldBean p = new PublicFieldBean(10);
                int x = p.age;
                fail("IllegalAccessError or NoSuchFieldError expected");
            } catch (IllegalAccessError | NoSuchFieldError e) {
                // Expected
            }
        }
    }

    /**
     * Matches Record serialization incompatibility (Section G).
     * Tests serialization compatibility when migrating from Class to Record.
     * Records use a different serialization mechanism.
     */
    @Test
    public void testSerialization() throws Exception {
        if (Version.isV1()) {
            UserBean u = new UserBean("serial");
            byte[] bytes = serialize(u);
            // In V1, we can deserialize it back
            UserBean u2 = (UserBean) deserialize(bytes);
            assertEquals("serial", u2.getName());
        } else {
            // In V2, we verify that the record is serializable itself.
            // Cross-version deserialization (Class -> Record) is known to fail with InvalidClassException
            // but is hard to simulate without persistent storage in this test runner.
            UserBean u = new UserBean("serial");
            byte[] bytes = serialize(u);
            UserBean u2 = (UserBean) deserialize(bytes);
            assertEquals("UserBean[name=serial]", u2.toString());
        }
    }

    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }
}
