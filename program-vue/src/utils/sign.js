import * as crypto from 'crypto'

const PRIVATE_KEY = '-----BEGIN PRIVATE KEY-----\n' +
  'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANwbf+S+I1i54rTY\n' +
  'e67X/Lu4Z/kMbgKGuGqRt8CrM/l9pCApwFl5FGSBeuDpBP26zEKkGka+TF+pVHfh\n' +
  'DtTmpgVjznI5sANKSXa23vuD14yJWUvmgpgAg+XNDOKR78wPouucQc7KnQiurxCh\n' +
  'nmB2elDfInZZ0iPBtAvM+sW08UDVAgMBAAECgYAUxAdp5kJhcZAg7belhD0U3M36\n' +
  'YiDS3jDx5POIGt7Zb/AXFLlP96tj3A9ivrk40vHIa8EK4ZNFBy2v7ay/i08oUDTO\n' +
  'r0Lo1YSmc9G+nmIiPvFLQAWIzKpjn8zpBsBYKsBSyAjq8vrAa4r9KTalF5ruNgEx\n' +
  'WXXC8bqegYyWW0UnwQJBAPU27hTLHB8CGi7zyayS6jTjdakenUXsbrYoBiK0bvB5\n' +
  '6QnFg0A2cXCu6HUTKcDx/7DENgWbjw4SKcPGi1mICgUCQQDlyd4nOsVIcsuZF9nv\n' +
  'arMYZwGeM6CLAsWf6ylgeg107b00t+A54fUs4kaOWQUDnoRmfr0X14A6Vk+ql6R7\n' +
  'JYSRAkBBmCcJwudL2Cke5DHPiyFBcpMX4Uua18spyP0TLYb7pvDSn1YjyCyCQxeF\n' +
  'sdGafmGybFozF9Clp/AqIaNHGN/tAkEAxZu3DtdspuQJkItBYLHKeHbEnm7ZZhIp\n' +
  'L2BFAfGUNvTn3Dkwe7aEaGfiszF8rWMZiyb8qE8rt39YHWUxDrHx8QJAPhkcNM37\n' +
  '7QqykmeDh+t6jH9tzFfpZw2qRdjUEld26bx+BUwHxqukq6edLfZ6uf5U9dmOfCvP\n' +
  'EEQY/tHQcYcitg==\n' +
  '-----END PRIVATE KEY-----'

export const generateSign = (signstr) => {
  const sign = crypto.createSign('md5WithRSAEncryption')
  sign.write(signstr, 'utf8')
  sign.end()
  return sign.sign(PRIVATE_KEY, 'base64')
}
