#include "include/thermalprint/thermalprint_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "thermalprint_plugin.h"

void ThermalprintPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  thermalprint::ThermalprintPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
