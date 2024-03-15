import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'thermalprint_method_channel.dart';

abstract class ThermalprintPlatform extends PlatformInterface {
  /// Constructs a ThermalprintPlatform.
  ThermalprintPlatform() : super(token: _token);

  static final Object _token = Object();

  static ThermalprintPlatform _instance = MethodChannelThermalprint();

  /// The default instance of [ThermalprintPlatform] to use.
  ///
  /// Defaults to [MethodChannelThermalprint].
  static ThermalprintPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ThermalprintPlatform] when
  /// they register themselves.
  static set instance(ThermalprintPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }
  Future<List<String>> getList() {
    throw UnimplementedError('getList() has not been implemented.');
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<dynamic?> invokeMethod(String name, [dynamic arguments]) {
    throw UnimplementedError('invokeMethod() has not been implemented.');
  }
}
