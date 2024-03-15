import 'thermalprint_platform_interface.dart';
export 'thermalprint_method_channel.dart';

class Thermalprint {
  Future<dynamic> invokeMethod(String name, [dynamic arguments]) {
    return ThermalprintPlatform.instance.invokeMethod(name, arguments);
  }

  Future<List<String>> getList() async {
    try {
      return ThermalprintPlatform.instance.getList();
    } catch (err) {
      return [];
    }
  }

  Future<String?> getPlatformVersion() {
    return ThermalprintPlatform.instance.getPlatformVersion();
  }
}
