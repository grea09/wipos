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
DEPENDANCIES_DIR 		 = $(COMPILATION_DIR)/dep
SOURCES_DIR 			 = src
HEADERS_DIR 			 = include
DOCUMENTATION_DIR 		 = doc
DATA_DIR 				 = data
ETC_DIR 				 = etc
MODULES_DIR 			 = modules
PLATFORM_DIR 			 = platform

## Space variable
NOTHING = 
SPACE =$(NOTHING) $(NOTHING)

## Subdirectories definitions
SOURCES_SUBDIR			:= ${sort ${dir ${wildcard ${SOURCES_DIR}/*/*/}}}
HEADERS_SUBDIR			:= ${sort ${dir ${wildcard ${HEADERS_DIR}/*/*/}}}
OBJECTS_SUBDIR			:= $(patsubst $(SOURCES_DIR)/%,$(OBJECTS_DIR)/%,$(SOURCES_SUBDIR))
DEPENDANCIES_SUBDIR		:= $(patsubst $(SOURCES_DIR)/%,$(DEPENDANCIES_DIR)/%,$(SOURCES_SUBDIR))

## VPath definitions
VPATH					 = $(SOURCES_SUBDIR)

## File extensions definitions
SOURCE_EXT 				 = c
HEADER_EXT 				 = h
OBJECTS_EXT 			 = o
DEPENDANCY_EXT 			 = d
MODULES_EXT 			 = inc

## Compilator definitions
CXX 					 = $(PLATFORM_DIR)/bin/mipsel-linux-gcc
DEFINES					 = 
HEADERS_SUPL			 = -I$(PLATFORM_DIR)/include/ -I$(PLATFORM_DIR)/usr/include/
CXX_FLAGS 				 = -Wall -O3
INCLUDES				 = $(HEADERS_SUPL) -I $(HEADERS_DIR) 
ifneq ($(strip $(HEADERS_SUBDIR)),)
INCLUDES				+= $(subst $(SPACE),$(SPACE)-I$(SPACE),$(SPACE)$(HEADERS_SUBDIR))
endif 
L_FLAGS					 = 
LIBRARIES				 = -L$(PLATFORM_DIR)/lib/ -L$(PLATFORM_DIR)/usr/lib/ -lpcap -lm -lpthread


## Targets definitions
SOURCES					 = $(wildcard $(SOURCES_DIR)/*.$(SOURCE_EXT))
SOURCES 				+= $(foreach sdir,$(SOURCES_SUBDIR),$(wildcard $(sdir)*.$(SOURCE_EXT)))
HEADERS					 = $(wildcard $(HEADERS_DIR)/*.$(HEADER_EXT))
HEADERS 				+= $(foreach sdir,$(HEADERS_SUBDIR),$(wildcard $(sdir)*.$(HEADER_EXT)))
OBJECTS 				:= $(patsubst $(SOURCES_DIR)/%.$(SOURCE_EXT),$(OBJECTS_DIR)/%.$(OBJECTS_EXT),$(SOURCES))
DEPENDANCIES 			:= $(patsubst $(SOURCES_DIR)/%.$(SOURCE_EXT),$(DEPENDANCIES_DIR)/%.$(DEPENDANCY_EXT),$(SOURCES))
MODULES					 = $(wildcard $(MODULES_DIR)/*.$(MODULES_EXT))
BINARY 					 = $(notdir $(subst $(SPACE),,$(PWD)))
BINARY 					:= $(addprefix $(BINARY_DIR)/, $(BINARY))

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

ifneq (`ls | grep $(DEPENDANCIES_DIR:$(CURRENT_DIR)/%=%)`,)
$(DEPENDANCIES) : $(DEPENDANCIES_DIR)
endif

ifneq (`ls | grep $(DEPENDANCIES_DIR:$(CURRENT_DIR)/%=%)`,)
$(DEPENDANCIES) : $(DEPENDANCIES_SUBDIR)
endif

ifneq (`ls | grep $(SOURCES_DIR:$(CURRENT_DIR)/%=%)`,)
$(SOURCES) : $(SOURCES_DIR)
endif


## Files dependancies

$(DEPENDANCIES_DIR)/%.$(DEPENDANCY_EXT): $(SOURCES_DIR)/%.$(SOURCE_EXT)
	@echo "$(DEPENDENCY_MSG) $< $(TO_MSG) $@ $(PENDING_MSG)"
	@touch $@
	@$(CXX) $(INCLUDES) -MM -MF $@ -MP -MT $(patsubst $(SOURCES_DIR)/%.$(SOURCE_EXT),$(OBJECTS_DIR)/%.$(OBJECTS_EXT),$<) $<

-include $(DEPENDANCIES)


## Modules rules

-include $(MODULES)

## Compilation rules

$(BINARY): $(OBJECTS)
ifneq ($(OBJECTS),)
	@echo "$(BINARY_MSG) de $< $(TO_MSG) $@ $(PENDING_MSG)"
	@$(CXX) $(L_FLAGS) -o $@ $(OBJECTS) $(LIBRARIES)
endif

$(OBJECTS_DIR)/%.$(OBJECTS_EXT): $(SOURCES_DIR)/%.$(SOURCE_EXT)
	@echo "$(OBJECT_MSG) $< $(TO_MSG) $@ $(PENDING_MSG)"
	@$(CXX) $(CXX_FLAGS) $(INCLUDES) $(DEFINES) -o $@ -c $< 


## Directories creation rules

$(SOURCES_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@echo "$(SOURCE_MSG) $@ $(BEFORE_MAKE_MSG)"
	@mkdir -p $@ 2> /dev/null
	
$(HEADERS_DIR) : 
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@echo "$(HEADER_MSG) $@ $(BEFORE_MAKE_MSG)"
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

$(DEPENDANCIES_DIR) :
	@echo "$(DIR_MSG) $@ $(PENDING_MSG)"
	@mkdir -p $@ 2> /dev/null

$(DEPENDANCIES_SUBDIR) : $(DEPENDANCIES_DIR)
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

checkdirs: $(SOURCES_DIR) $(HEADERS_DIR)

dependancies: $(DEPENDANCIES)

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
	scp $(BINARY) root@192.168.1.1:

clean:
	@echo "$(CLEAN_MSG)"
	@rm -rf $(COMPILATION_DIR)/*
	@rm -rf $(BINARY_DIR)/*
 
mrproper: clean
	@echo "$(MRPROPER_MSG)"
	@rmdir --ignore-fail-on-non-empty $(COMPILATION_DIR)

install: $(BINARY)
	@echo "$(INSTALL_MSG)"
	@cp -R $< $(INSTALL_DIR)
