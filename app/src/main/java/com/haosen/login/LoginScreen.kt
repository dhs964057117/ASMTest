package com.haosen.login

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haosen.asmtest.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.haosen.asmtest.App
import com.haosen.test.TestActivity

@Preview(showBackground = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    onPopBackStackToMain: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Image(
            painter = painterResource(R.drawable.img_login_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = "",
            modifier = Modifier
                .height(24.dp)
                .background(Color(0x6E000000))
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .align(Alignment.TopCenter),
            color = Color(0xffffffff)
        )
        val userNameText = remember {
            mutableStateOf("")
        }
        val passwordText = remember {
            mutableStateOf("")
        }
        ElevatedCard(
            modifier = Modifier
                .wrapContentSize()
                .padding(100.dp, 300.dp, 100.dp, 0.dp),
            colors = CardColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
                hoveredElevation = 10.dp
            )
        ) {
            Column {
                OutlinedTextField(
                    value = userNameText.value,
                    onValueChange = {
                        userNameText.value = it
                    },
                    modifier = Modifier
                        .padding(20.dp, 20.dp, 20.dp, 0.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(text = "请输入账号") })

                OutlinedTextField(
                    value = passwordText.value,
                    onValueChange = {
                        passwordText.value = it
                    },
                    modifier = Modifier
                        .padding(20.dp, 20.dp, 20.dp, 0.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(text = "请输入密码") })
                LoginButton()
                Row(modifier = Modifier.padding(top = 10.dp, start = 40.dp)) {
                    Text(
                        text = "注册",
                        color = Color(0xD8FFFFFF),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable(
                            indication = null,
                            onClick = { },
                            interactionSource = remember {
                                MutableInteractionSource()
                            })
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "忘记密码？",
                        color = Color(0xD8FFFFFF),
                        fontSize = 14.sp,
                        modifier = Modifier.clickable(
                            indication = null,
                            onClick = { },
                            interactionSource = remember {
                                MutableInteractionSource()
                            })
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 100.dp, start = 40.dp, end = 40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 5.dp),
                        color = Color(0xD8FFFFFF),
                        thickness = 1.dp
                    )
                    Text(
                        "第三方账号登录", color = Color(0xD8FFFFFF),
                        fontSize = 14.sp,
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp),
                        color = Color(0xD8FFFFFF),
                        thickness = 1.dp
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 20.dp, start = 80.dp, end = 80.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.svg_wechat),
                        contentDescription = "weChat",
                        Modifier
                            .clickable(
                                indication = null,
                                onClick = { },
                                interactionSource = remember {
                                    MutableInteractionSource()
                                })
                            .size(60.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painterResource(R.drawable.qq),
                        contentDescription = "QQ",
                        Modifier
                            .clickable(
                                indication = null,
                                onClick = { },
                                interactionSource = remember {
                                    MutableInteractionSource()
                                })
                            .size(60.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painterResource(R.drawable.paypal),
                        contentDescription = "paypal",
                        Modifier
                            .clickable(
                                indication = null,
                                onClick = { },
                                interactionSource = remember {
                                    MutableInteractionSource()
                                })
                            .size(53.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginButton() {
    Button(
        onClick = {
            App.application.startActivity(Intent(App.application, TestActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(48.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onBackground)
    ) {
        Text(
            text = "Log in", style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}