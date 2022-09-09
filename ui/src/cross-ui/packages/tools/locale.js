import Config from './config'

export default {
    language(code, lower) {
      if (Config.languages.map(l => l.code).includes(code)) {
          return lower ? code.toLowerCase() : code
      }
      if (code.indexOf('zh') !== -1 && code !== Config.languages[1].code){
          return lower ? Config.languages[2].code.toLowerCase() : Config.languages[2].code
      }
      return lower ? Config.languages[0].code.toLowerCase() : Config.languages[0].code
    }
}