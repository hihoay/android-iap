#!/bin/bash

# Script to generate or delete files for a new entity based on Clean Architecture.
# Usage: bash ./template.sh <entity_name> [-c|-d] <module:app>
#   -c: create files
#   -d: delete files
#
#!/bin/bash

# Script to generate or delete files for a new entity based on Clean Architecture.
# Usage: bash ./template.sh <entity_name> [-c|-d]
#   -c: create files
#   -d: delete files

# --- Configuration ---
entity_name=$1
how=$2
module=$3
root_dir="$module/src/main/java/taymay/$module/"
root_dir_test="$module/src/test/java"

# --- Validation ---
if [ -z "$entity_name" ] || [ -z "$how" ]; then
    echo "Error: Missing arguments."
    echo "Usage: $0 <entity_name> [-c|-d]"
    exit 1
fi

if [ "$how" != "-c" ] && [ "$how" != "-d" ]; then
    echo "Error: Invalid flag '$how'. Use -c (create) or -d (delete)."
    echo "Usage: $0 <entity_name> [-c|-d]"
    exit 1
fi


# --- Helper Functions ---
# Converts snake_case to UpperCamelCase
to_upper_camel_case() {
    echo "$1" | awk -F '_' '{for (i=1; i<=NF; i++) $i=toupper(substr($i,1,1)) tolower(substr($i,2));}1' OFS=""
}

upper_entity_name=$(to_upper_camel_case "$entity_name")


# --- File Definitions ---
# Using an array makes it easier to manage the list of files.
files_to_manage=(
    "$root_dir/entities/${upper_entity_name}.kt"
    "$root_dir/usecases/${upper_entity_name}Usecase.kt"
    "$root_dir/repositories/${upper_entity_name}Repository.kt"
    "$root_dir/containers/${upper_entity_name}Container.kt"
    "$root_dir/adaptors/mapper/${upper_entity_name}Mapper.kt"
    "$root_dir/adaptors/datasources/local/${upper_entity_name}LocalDataSource.kt"
    "$root_dir/adaptors/datasources/remote/${upper_entity_name}RemoteDataSource.kt"
    "$root_dir/adaptors/repositories/${upper_entity_name}RepositoryImpl.kt"
)

# --- Main Logic ---

# Deletion
if [ "$how" == "-d" ]; then
    echo "Deleting files for entity: $upper_entity_name..."
    for file_path in "${files_to_manage[@]}"; do
        if [ -f "$file_path" ]; then
            rm "$file_path"
            echo "deleted $file_path"
        fi
    done
    echo "Deletion complete."
    exit 0
fi


# Creation
if [ "$how" == "-c" ]; then
    echo "Creating files for entity: $upper_entity_name..."

    # Create all directories at once
    mkdir -p \
        "$root_dir/entities" \
        "$root_dir/usecases" \
        "$root_dir/repositories" \
        "$root_dir/containers" \
        "$root_dir/adaptors/mapper" \
        "$root_dir/adaptors/datasources/local" \
        "$root_dir/adaptors/datasources/remote" \
        "$root_dir/adaptors/repositories"

    # --- File Generation using Heredocs for better readability ---

    echo "creating $root_dir/entities/${upper_entity_name}.kt"
    cat <<-EOF > "$root_dir/entities/${upper_entity_name}.kt"
package taymay.${module}.entities

data class ${upper_entity_name}(
    val _id: Long,
)
EOF

    echo "creating $root_dir/usecases/${upper_entity_name}Usecase.kt"
    cat <<-EOF > "$root_dir/usecases/${upper_entity_name}Usecase.kt"
package taymay.${module}.usecases

import taymay.${module}.adaptors.mapper.${upper_entity_name}Mapper
import taymay.${module}.repositories.${upper_entity_name}Repository
import taymay.usecases.EntityUsecase

class ${upper_entity_name}Usecase(private val repository${upper_entity_name}: ${upper_entity_name}Repository) :
    EntityUsecase<${upper_entity_name}Mapper>(repository${upper_entity_name}) {
}
EOF

    echo "creating $root_dir/repositories/${upper_entity_name}Repository.kt"
    cat <<-EOF > "$root_dir/repositories/${upper_entity_name}Repository.kt"
package taymay.${module}.repositories

import taymay.adaptors.repositories.EntityRepositoryImpl

import android.content.Context
import taymay.${module}.adaptors.datasources.local.${upper_entity_name}LocalDataSource
import taymay.${module}.adaptors.datasources.remote.${upper_entity_name}RemoteDataSource
import taymay.${module}.adaptors.mapper.${upper_entity_name}Mapper

abstract class ${upper_entity_name}Repository (
    context: Context,
    localDataSource: ${upper_entity_name}LocalDataSource,
    remoteDataSource: ${upper_entity_name}RemoteDataSource
) : EntityRepositoryImpl<${upper_entity_name}Mapper>(context,localDataSource,remoteDataSource) {

}


EOF

    echo "creating $root_dir/containers/${upper_entity_name}Container.kt"
    cat <<-EOF > "$root_dir/containers/${upper_entity_name}Container.kt"




package taymay.${module}.containers

import taymay.${module}.usecases.${upper_entity_name}Usecase
import taymay.containers.RepositoryContainer
import taymay.${module}.adaptors.datasources.local.${upper_entity_name}LocalDataSource
import taymay.${module}.adaptors.datasources.remote.${upper_entity_name}RemoteDataSource
import taymay.${module}.adaptors.repositories.${upper_entity_name}RepositoryImpl

fun RepositoryContainer.get${upper_entity_name}Repository(): ${upper_entity_name}RepositoryImpl {
    return ${upper_entity_name}RepositoryImpl(
        context, ${upper_entity_name}LocalDataSource(), ${upper_entity_name}RemoteDataSource()
    )
}

class ${upper_entity_name}Container(repositoryContainer: RepositoryContainer) {
    var usecase: ${upper_entity_name}Usecase
        init {

//            var ${upper_entity_name}Repository: ${upper_entity_name}RepositoryImpl

//            ${upper_entity_name}Repository = ${upper_entity_name}RepositoryImpl(
//                context = context,
//                localDataSource = ${upper_entity_name}LocalDataSource(),
//                remoteDataSource = ${upper_entity_name}RemoteDataSource()
//            )

            usecase = ${upper_entity_name}Usecase(repositoryContainer.get${upper_entity_name}Repository())
        }



}







EOF

    echo "creating $root_dir/adaptors/mapper/${upper_entity_name}Mapper.kt"
    cat <<-EOF > "$root_dir/adaptors/mapper/${upper_entity_name}Mapper.kt"
package taymay.${module}.adaptors.mapper

import com.google.gson.reflect.TypeToken
import taymay.adaptors.mapper.Mappable.Companion.gson
import taymay.${module}.entities.${upper_entity_name}
import taymay.adaptors.mapper.Mapper

class ${upper_entity_name}Mapper() : Mapper<${upper_entity_name}>() {
    var _id: Long = 0
    override fun toEntity(): ${upper_entity_name} {
        return ${upper_entity_name}(
          _id,
        )
    }

    override fun toJson(): String {
        return gson.toJson(toEntity())
    }

    override fun toMap(): Map<String, Any> {
        return mapOf(
          "_id" to _id,
        )
    }
}
EOF

    echo "creating $root_dir/adaptors/datasources/local/${upper_entity_name}LocalDataSource.kt"
    cat <<-EOF > "$root_dir/adaptors/datasources/local/${upper_entity_name}LocalDataSource.kt"
package taymay.${module}.adaptors.datasources.local
import taymay.adaptors.datasources.local.EntityLocalDataSource
import taymay.adaptors.datasources.remote.EntityRemoteDataSource

import taymay.adaptors.mapper.Mappable
import taymay.${module}.adaptors.mapper.${upper_entity_name}Mapper
import taymay.${module}.entities.${upper_entity_name}
import taymay.frameworks.database.JsonDatabase
import taymay.frameworks.utils.AppEnvironment
import java.io.File
import java.util.logging.Logger

class ${upper_entity_name}LocalDataSource(
     box: JsonDatabase<${upper_entity_name}Mapper>? = JsonDatabase(
            File(
                AppEnvironment.applicationContext?.filesDir!!.absolutePath,
                ${upper_entity_name}Mapper::class.java.name
            ).absolutePath, ${upper_entity_name}Mapper::class.java
        )
) : EntityLocalDataSource<${upper_entity_name}Mapper>(
    box!!,
    fromMap = { map: Map<String, Any> ->
        Mappable.fromMap(map, ${upper_entity_name}Mapper::class.java)
    },
    toMap = { userMapper: ${upper_entity_name}Mapper -> userMapper.toMap() },
) {
    override val logger = Logger.getLogger(${upper_entity_name}LocalDataSource::class.java.name)
}
EOF

    echo "creating $root_dir/adaptors/datasources/remote/${upper_entity_name}RemoteDataSource.kt"
    cat <<-EOF > "$root_dir/adaptors/datasources/remote/${upper_entity_name}RemoteDataSource.kt"
package taymay.${module}.adaptors.datasources.remote
import taymay.adaptors.datasources.local.EntityLocalDataSource
import taymay.adaptors.datasources.remote.EntityRemoteDataSource

import taymay.adaptors.mapper.Mappable
import taymay.${module}.adaptors.mapper.${upper_entity_name}Mapper
import java.util.logging.Logger
import taymay.frameworks.utils.AppEnvironment.SESSION_SERVICE_URL

class ${upper_entity_name}RemoteDataSource(
) : EntityRemoteDataSource<${upper_entity_name}Mapper>(
    "\${SESSION_SERVICE_URL}/api/v1/${entity_name}s",
    fromMap = { map: Map<String, Any> ->
        Mappable.fromMap(map, ${upper_entity_name}Mapper::class.java)
    },
    toMap = { userMapper: ${upper_entity_name}Mapper -> userMapper.toMap() },
    toJson = { userMapper: ${upper_entity_name}Mapper -> userMapper.toJson() },
    fromJson = { s: String -> Mappable.fromJson(s) },
    fromJsons = { s: String -> Mappable.fromJsons(s) }) {
    override var logger = Logger.getLogger(${upper_entity_name}RemoteDataSource::class.java.name)
}
EOF

    echo "creating $root_dir/adaptors/repositories/${upper_entity_name}RepositoryImpl.kt"
    cat <<-EOF > "$root_dir/adaptors/repositories/${upper_entity_name}RepositoryImpl.kt"
package taymay.${module}.adaptors.repositories

import android.content.Context
import taymay.${module}.adaptors.datasources.local.${upper_entity_name}LocalDataSource
import taymay.${module}.adaptors.datasources.remote.${upper_entity_name}RemoteDataSource
import taymay.${module}.adaptors.mapper.${upper_entity_name}Mapper
import kotlinx.coroutines.runBlocking
import taymay.${module}.repositories.${upper_entity_name}Repository

class ${upper_entity_name}RepositoryImpl(
    override var context: Context,
    override var localDataSource: ${upper_entity_name}LocalDataSource,
    override var remoteDataSource: ${upper_entity_name}RemoteDataSource,
) : ${upper_entity_name}Repository(context,localDataSource,remoteDataSource) {

    override fun create(data: ${upper_entity_name}Mapper): Long = runBlocking {
       localDataSource.create(data)
    }

    override fun update(_id: Long, data: ${upper_entity_name}Mapper): Boolean = runBlocking {
       localDataSource.update(_id, data)
    }


    override fun delete(_id: Long): Boolean = runBlocking {
       localDataSource.delete(_id)
    }

    override fun read(_id: Long): ${upper_entity_name}Mapper? = runBlocking {
       localDataSource.read(_id)
    }

    override fun reads(): List<${upper_entity_name}Mapper> = runBlocking {
       localDataSource.reads()
    }

    override fun count(): Long = runBlocking {
       localDataSource.count()
    }

    override fun exists(_id: Long): Boolean = runBlocking {
       localDataSource.exists(_id)
    }

    override fun sync() = runBlocking {

    }



    override fun addIfNotExist(
        item:${upper_entity_name}Mapper, predicate: (${upper_entity_name}Mapper) -> Boolean
    ): Boolean =runBlocking {
        localDataSource.addIfNotExist(item,predicate)
    }

    override fun addOrUpdate(item:${upper_entity_name}Mapper, predicate: (${upper_entity_name}Mapper) -> Boolean) =
        runBlocking {
            localDataSource.addOrUpdate(item, predicate)

        }

    override fun find(predicate: (${upper_entity_name}Mapper) -> Boolean): List<${upper_entity_name}Mapper> =
        runBlocking {
            localDataSource.find(predicate)
        }

    override fun forEach(action: (${upper_entity_name}Mapper) -> Unit) = runBlocking {
        localDataSource.forEach(action)
    }


}


EOF
    echo "Creation complete."
fi