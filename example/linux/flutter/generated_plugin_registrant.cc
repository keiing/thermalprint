//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <thermalprint/thermalprint_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) thermalprint_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "ThermalprintPlugin");
  thermalprint_plugin_register_with_registrar(thermalprint_registrar);
}
