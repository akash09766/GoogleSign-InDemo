package gendevs.com.googlesign_indemo.db;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

/**
 * Created by Akash on 09/02/16.
 */
public class DbConfigurationBuilder {

    public EmbeddedConfiguration getConfiguration() {

        EmbeddedConfiguration newConfiguration = Db4oEmbedded
                .newConfiguration();
        newConfiguration.common().activationDepth(DbService.GLOBAL_QUERY_DEPTH);
        newConfiguration.common().updateDepth(3);
        return newConfiguration;
    }
}
