# Variables
SRC_DIR = ../Bazaar
CLASS_DIR = Other/bin
TEST_DIR = $(SRC_DIR)/Tests
JAR_FILE = bazaar.jar
MANIFEST_FILE = MANIFEST.MF
GSON_JAR = Other/bin/gson-2.10.1.jar

# Find .java files
TEST_FILES := $(shell find "$(TEST_DIR)/JsonObjects" -name "*.java")
SRC_FILES := $(shell find $(SRC_DIR) -name "*.java" -not -path "$(TEST_DIR)/*") $(TEST_FILES)

# .class files
CLASS_FILES := $(SRC_FILES:$(SRC_DIR)/%.java=$(CLASS_DIR)/%.class)
CLASS_FILES += $(TEST_FILES:$(SRC_DIR)/Tests/JsonObjects/%.java=$(CLASS_DIR)/Tests/JsonObjects/%.class)

# Compile and package JAR
all: $(CLASS_DIR) setup $(JAR_FILE)

# Download dependencies
setup:
	bash Other/setup

$(CLASS_DIR):
	@mkdir -p $@

# Compile all Java files at once
$(CLASS_DIR)/%.class: $(SRC_DIR)/%.java  # Rule for creating .class files
	@mkdir -p $(dir $@)
	javac -cp "$(GSON_JAR):$(CLASS_DIR)" -d $(CLASS_DIR) $(SRC_FILES)  # Compile all at once

# Copy assets to the bin directory
copy-assets:
	@mkdir -p $(CLASS_DIR)/Common/Assets
	cp -r "$(SRC_DIR)/Common/Assets/smiley.png" $(CLASS_DIR)/Common/Assets/

# Create manifest file
$(MANIFEST_FILE):
	echo Class-Path: $(GSON_JAR) >> $(MANIFEST_FILE)

# Create JAR file
$(JAR_FILE): $(CLASS_FILES) $(MANIFEST_FILE) copy-assets
	jar --create --file $(JAR_FILE) --manifest $(MANIFEST_FILE) -C $(CLASS_DIR) .

# Clean up
clean:
	rm -rf $(CLASS_DIR) $(JAR_FILE) $(MANIFEST_FILE)
