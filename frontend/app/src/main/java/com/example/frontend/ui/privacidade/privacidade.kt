package com.example.frontend.ui.privacidade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontend.ui.components.CustomTopBar

@Composable
fun PoliticasPrivacidadeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CustomTopBar(
                navController = navController,
                title = "Política de Privacidade"
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            // TÍTULO
            Text(
                text = "Política de Privacidade — Mercado Académico",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Última atualização: Dez 2025",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // SEÇÃO 1
            Text(
                text = "1. Dados Recolhidos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
• Nome, email, telefone e foto de perfil  
• Produtos publicados e favoritos  
• Histórico de compras e vendas  
• Dados técnicos do dispositivo (modelo, IP local, idioma)
            """.trimIndent())
            Spacer16()

            // SEÇÃO 2
            Text(
                text = "2. Como Utilizamos os Dados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
• Criar e gerir a sua conta  
• Permitir compra e venda de produtos  
• Enviar emails de confirmação e notificações  
• Melhorar a segurança da plataforma  
• Prevenir fraudes e abusos
            """.trimIndent())
            Spacer16()

            // SEÇÃO 3
            Text(
                text = "3. Partilha de Dados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
Os seus dados podem ser partilhados apenas com:  
• Serviços essenciais, como Stripe (pagamentos)  
• Serviços de email (verificação e recuperação)  
• Autoridades, quando exigido por lei  

Nunca vendemos os seus dados.
            """.trimIndent())
            Spacer16()

            // SEÇÃO 4
            Text(
                text = "4. Segurança",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
• Utilizamos encriptação sempre que possível  
• O acesso aos dados é restrito  
• Token JWT protege a autenticação
            """.trimIndent())
            Spacer16()

            // SEÇÃO 5
            Text(
                text = "5. Direitos do Utilizador",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
Pode a qualquer momento:  
• Aceder aos seus dados  
• Solicitar correção  
• Eliminar a conta  
• Solicitar esclarecimentos sobre o tratamento das informações
            """.trimIndent())
            Spacer16()

            // SEÇÃO 6
            Text(
                text = "6. Cookies e Armazenamento",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
O app pode guardar:  
• Sessão de login  
• Preferências do utilizador  
• Dados de navegação básicos
            """.trimIndent())
            Spacer16()

            // SEÇÃO 7
            Text(
                text = "7. Alterações",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("""
Esta Política pode ser atualizada.  
Ao continuar a utilizar o app, aceita as mudanças.
            """.trimIndent())
            Spacer16()

            // CONTACTO
            Text(
                text = "📩 Contacto",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text("mercadoacademico@outlook.pt")
            Spacer16()
        }
    }
}

@Composable
fun Spacer16() {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(vertical = 12.dp))
}
