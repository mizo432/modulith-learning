@org.springframework.modulith.ApplicationModule(id = "common", displayName = "共通ユーティリティモジュール", type = org.springframework.modulith.ApplicationModule.Type.OPEN)
@org.springframework.modulith.NamedInterface(name = {"dateProvider", "primitive", "precondition", "functional", "annotation", "io", "ipaddress"})
/**
 * 共通ユーティリティクラスを格納するモジュールです。
 *
 * <p>プリミティブ拡張、事前条件チェック、日付プロバイダーなど、プロジェクト全体で共有される
 * ユーティリティクラスを提供します。
 */
package com.github.mizo432.shared.common;
