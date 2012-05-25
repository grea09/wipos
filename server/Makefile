#	Makefile Générique, sortie en Français explicite.
#	
#	Copyright 2010 Antoine GRÉA <grea09@gmail.com>
#	
#	This program is free software; you can redistribute it and/or modify
#	it under the terms of the GNU General Public License as published by
#	the Free Software Foundation; either version 2 of the License, or
#	(at your option) any later version.
#	
#	This program is distributed in the hope that it will be useful,
#	but WITHOUT ANY WARRANTY; without even the implied warranty of
#	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#	GNU General Public License for more details.
#	
#	You should have received a copy of the GNU General Public License
#	along with this program; if not, write to the Free Software
#	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#	MA 02110-1301, USA.

## Language definition

# French
TO_MSG = vers
PENDING_MSG = ...

DEPENDENCY_MSG = Génération des dépendances de
MODULE_MSG = Compilation du module
BINARY_MSG = Édition des liens de
OBJECT_MSG = Compilation de
DIR_MSG = Création de

SOURCE_MSG = Écrivez vos sources dans
HEADER_MSG = Écrivez vos fichiers d'en-tête dans
BEFORE_MAKE_MSG = avant de faire make !

EXEC_MSG = ÉXÉCUTION !
CLEAN_MSG = Nettoyage de primtemps ...
MRPROPER_MSG = Sa magie c'est sa puissance !
INSTALL_MSG = Installation ...



## Directories definitions
#~ CURRENT_DIR 			 = .
INSTALL_DIR 			 = /usr/bin
BINARY_DIR 				 = bin
COMPILATION_DIR 		 = build
OBJECTS_DIR 			 = $(COMPILATION_DIR)/obj
SOURCES_DIR 			 = src
DOCUMENTATION_DIR 		 = doc
DATA_DIR 				 = data
ETC_DIR 				 = etc
LIBRARIES_DIR 			 = lib

## Space variable
NOTHING = 
SPACE =$(NOTHING) $(NOTHING)

## Subdirectories definitions
SOURCES_SUBDIR			:= ${sort ${dir ${wildcard ${SOURCES_DIR}/*/*/}}}
OBJECTS_SUBDIR			:= $(patsubst $(SOURCES_DIR)/%,$(OBJECTS_DIR)/%,$(SOURCES_SUBDIR))

## VPath definitions
VPATH					 = $(SOURCES_SUBDIR)

## File extensions definitions
SOURCE_EXT 				 = java
OBJECTS_EXT 			 = class
BINARY_EXT 				 = jar

## Compilator definitions
CXX 					 = javac
JAR						 = jar
CXX_FLAGS 				 = -g
JAR_FLAGS				 = cf


## Targets definitions
SOURCES					 = $(wildcard $(SOURCES_DIR)/*.$(SOURCE_EXT))
SOURCES 				+= $(foreach sdir,$(SOURCES_SUBDIR),$(wildcard $(sdir)*.$(SOURCE_EXT)))
OBJECTS 				:= $(patsubst $(SOURCES_DIR)/%.$(SOURCE_EXT),$(OBJECTS_DIR)/%.$(OBJECTS_EXT),$(SOURCES))
BINARY 					 = $(notdir $(subst $(SPACE),,$(PWD)))
BINARY 					:= $(addprefix $(BINARY_DIR)/, $(BINARY)).$(BINARY_EXT)

## Main target
all: checkdirs dependancies modules binary

## Directories dependancies
ifneq (`ls | grep $(BINARY_DIR:$(CURRENT_DIR)/%=%)`,)
$(BINARY) : $(BINARY_DIR)
endif

ifneq (`ls | grep $(OBJECTS_DIR:$(CURRENT_DIR)/%=%)`,)
$(OBJECTS) : $(OBJECTS_DIR)
endif

ifneq (`ls | grep $(OBJECTS_DIR:$(OBJECTS_DIR)/%=%)`,)
$(OBJECTS) : $(OBJECTS_SUBDIR)
endif

ifneq (`ls | grep $(SOURCES_DIR:$(CURRENT_DIR)/%=%)`,)
$(SOURCES) : $(SOURCES_DIR)
endif

## Compilation rules

$(BINARY): $(OBJECTS)
ifneq ($(OBJECTS),)
	@echo "$(BINARY_MSG) de $< $(TO_MSG) $@ $(PENDING_MSG)"
	@$(JAR) $(JAR_FLAGS) $@ $(OBJECTS) $(DATA_DIR) $(LIBRARIES_DIR)
endif

$(OBJECTS_DIR)/%.$(OBJECTS_EXT): $(SOURCES_DIR)/%.$(SOURCE_EXT)
	@echo "$(OBJECT_MSG) $< $(TO_MSG) $@ $(PENDING_MSG)"
	@$(CXX) $(CXX_FLAGS) $< -d $(OBJECTS_DIR) 


## Directories creation rules

$(SOURCES_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@echo "$(SOURCE_MSG) $@ $(BEFORE_MAKE_MSG)"
	@mkdir -p $@ 2> /dev/null

$(COMPILATION_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(OBJECTS_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(OBJECTS_SUBDIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(BINARY_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(DOCUMENTATION_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(DATA_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(ETC_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

## Conveniance rules

## Phony rules
.PHONY = clean mrproper exec all debug checkdirs modules doc etc data alldirs dependancies

alldirs : $(DOCUMENTATION_DIR) $(ETC_DIR) $(DATA_DIR) checkdirs

checkdirs: $(SOURCES_DIR)

modules: $(MODULES)

binary: $(BINARY)

debug:
	@echo "PWD = $(PWD)"
	@echo "SOURCES_DIR = $(SOURCES_DIR)"
	@echo "SOURCES_SUBDIR = $(SOURCES_SUBDIR)"
	@echo "OBJECTS_SUBDIR = $(OBJECTS_SUBDIR)"
	@echo "SOURCES = $(SOURCES)"
	@echo "OBJECTS = $(OBJECTS)"
	@echo "MODULES_DIR = $(MODULES_DIR)"
	@echo "MODULES_SUBDIR = $(MODULES_SUBDIR)"
	@echo "MODULES = $(MODULES)"
	@echo "BINARY = $(BINARY)"

exec: $(BINARY)
	@echo "$(EXEC_MSG)"
	@java $(BINARY)

clean:
	@echo "$(CLEAN_MSG)"
	@rm -rf $(COMPILATION_DIR)/*
	@rm -rf $(BINARY_DIR)/*
 
mrproper: clean
	@echo "$(MRPROPER_MSG)"
	@rmdir --ignore-fail-on-non-empty $(COMPILATION_DIR)

#~ install: $(BINARY)
	#~ @echo "$(INSTALL_MSG)"
	#~ @cp -R $< $(INSTALL_DIR)
