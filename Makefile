JAVAC = javac
JAVA = java
JAR = jar
MAIN_CLASS = Main

SRC_DIR=src
ASSETS_DIR=assets
OUT_DIR=out

JAR_NAME = flappy.jar

JAVA_FILES = $(wildcard $(SRC_DIR)/*.java)
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java,$(OUT_DIR)/%.class,$(JAVA_FILES))

.PHONY: clean run build

default: build

$(OUT_DIR):
	@mkdir -p $(OUT_DIR)

$(ASSETS_DIR): $(OUT_DIR)
	@cp -r $(SRC_DIR)/$(ASSETS_DIR) $(OUT_DIR)/
	@rm -rf $(OUT_DIR)/$(ASSETS_DIR)/*.wav

$(OUT_DIR)/%.class: $(SRC_DIR)/%.java $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) -cp $(SRC_DIR) $<

$(JAR_NAME): $(CLASS_FILES)
	$(JAR) vcfe $(JAR_NAME) $(MAIN_CLASS) -C $(OUT_DIR) .

build: $(OUT_DIR) $(ASSETS_DIR) $(JAR_NAME)

run: build
	$(JAVA) -jar $(JAR_NAME)

clean:
	@rm -rf $(OUT_DIR) $(JAR_NAME)