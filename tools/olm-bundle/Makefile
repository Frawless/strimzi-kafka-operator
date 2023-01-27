PROJECT_NAME=olm-bundle

BUNDLE_VERSION ?= $(shell cat $(dirname $(realpath $0))../../release.version | cut -d'-' -f1)
ARCH ?= amd64
BUNDLE_IMAGE ?= quay.io/jstejska/operator-bundle
BUNDLE_IMAGE_TAG ?= latest
INDEX_IMAGE ?= quay.io/jstejska/operator-iib
INDEX_IMAGE_TAG ?= latest
BUILD_ENGINE ?= podman
.DEFAULT_GOAL := default

include ../../Makefile.docker

olm_install:
	BUILD_ENGINE=$(BUILD_ENGINE) ./generate-olm-bundle.sh BUNDLE_VERSION=$(BUNDLE_VERSION) DOCKER_REGISTRY=$(DOCKER_REGISTRY) DOCKER_ORG=$(DOCKER_ORG) DOCKER_TAG=$(DOCKER_TAG) BUNDLE_IMAGE=$(BUNDLE_IMAGE) BUNDLE_IMAGE_TAG=$(BUNDLE_IMAGE_TAG) INDEX_IMAGE=$(INDEX_IMAGE)

default: olm_install
