#!/bin/bash
set -e

generate() {
local output_dir=$1
local spec=$2
local language=$3
local library=$4
local config="${@:5}"
  swagger-codegen generate -o ${output_dir} -i ${spec} -l ${language} --library ${library} $config
}

base_path=forgerock-openbanking-uk-aspsp-rs/forgerock-openbanking-uk-aspsp-rs-mock-store
output_dir=$1
language=$2
library=$3
config="${@:4}"

if [[ -z ${output_dir+x} || -z ${language+x} || -z ${library+x} || -z ${config+x} ]]; then
  echo "Missing arguments"
  echo
  echo "output: ${output_dir}"
  echo "language: ${language}"
  echo "library: ${library}"
  echo "config: ${config}"
  echo
  echo "Example usage:"
  echo "    generate-bank-resource-server.sh bank-rs-store spring spring-mvc --group-id com.forgerock.openbanking -DuseBeanValidation=true -DinterfaceOnly=true"
  exit 1
fi

generate ${output_dir} ${base_path}/internal.json ${language} ${library} "--model-package com.forgerock.rs.store.models.internal --api-package=com.forgerock.rs.store.apis.internal ${config}"
generate ${output_dir} ${base_path}/openbanking-v1.1.json ${language} ${library} "--model-package com.forgerock.rs.store.models.v1_0 --api-package=com.forgerock.rs.store.apis.v1_0 ${config}"
generate ${output_dir} ${base_path}/openbanking-v2.0.json ${language} ${library} "--model-package com.forgerock.rs.store.models.v2_0 --api-package=com.forgerock.rs.store.apis.v2_0 ${config}"
generate ${output_dir} ${base_path}/openbanking-v3.0.json ${language} ${library} "--model-package com.forgerock.rs.store.models.v3_0 --api-package=com.forgerock.rs.store.apis.v3_0 ${config}"
generate ${output_dir} ${base_path}/openbanking-v3.1.1.json ${language} ${library} "--model-package com.forgerock.rs.store.models.v3.1.1 --api-package=com.forgerock.rs.store.apis.v3_1_1 ${config}"
generate ${output_dir} ${base_path}/openbanking-v3.1.2.json ${language} ${library} "--model-package com.forgerock.rs.store.models.v3_1_2 --api-package=com.forgerock.rs.store.apis.v3_1_2 ${config}"
generate ${output_dir} ${base_path}/openbanking-v3.1.json ${language} ${library} "--model-package com.forgerock.rs.store.models.v3_1 --api-package=com.forgerock.rs.store.apis.v3_1 ${config}"
