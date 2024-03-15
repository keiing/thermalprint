import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'thermalprint_platform_interface.dart';

/// An implementation of [ThermalprintPlatform] that uses method channels.
class MethodChannelThermalprint extends ThermalprintPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('thermalprint');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<List<String>> getList() async {
    try {
      final List? value = await methodChannel.invokeMethod<List>(
        "getList",
      );
      return value
              ?.map(
                (e) => e.toString(),
              )
              .toList() ??
          [];
    } catch (err) {
      print(err);
      return [];
    }
  }

  Future<dynamic> invokeMethod(String name, [dynamic arguments]) {
    return methodChannel.invokeMethod<dynamic>(name, arguments);
  }
}
